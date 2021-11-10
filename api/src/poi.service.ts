import { Injectable, OnModuleInit } from '@nestjs/common';
import { PoI, DetailedPoI, ExternalPoI} from './PoI';
import { HttpService } from '@nestjs/axios';
import { firstValueFrom, map } from 'rxjs';

@Injectable()
export class PoIService implements OnModuleInit {

    private storage: DetailedPoI[] = [];

    constructor(private readonly httpService: HttpService) {}

    /**
     * Function that loads all PoIs of the dataset
     */
    async onModuleInit() {
       const apiPoIs = await firstValueFrom(
        this.httpService
          .get<ExternalPoI[]>('https://api.npoint.io/01b169adea931451edce')
          .pipe(
            map((response) =>
              response.data.map((apiPoI) => ({
                name : apiPoI.Name,
                place: apiPoI.Lieu,
                latitude: Number(apiPoI.GPS_Coord.split(",")[0]),
                longitude: Number(apiPoI.GPS_Coord.split(",")[1]),
                commentary: apiPoI.Commentaires,
                level: apiPoI.Niveau,
                type: apiPoI.Type,
                pictureLink: apiPoI.gx_media_links,
                favorite: false
              })),
            ),
          ),
      );
        apiPoIs.forEach(poi => {this.addPoI(poi)});
    }

    /**
     * Pushes a PoI into the list of PoIs 'storage'
     */
    addPoI(detailedPoI: DetailedPoI): void {
        if (!(this.storage.some(value => value === detailedPoI))) {
            this.storage.push(detailedPoI)
        }
    }

    /**
     * Function that creates a list of abridged data for the PoIs
     */
    getAllPoIs() {
        let poisToSend: PoI [] = [];
        this.storage.forEach(detailedPoI => poisToSend.push(this.createPoIAbridged(detailedPoI)))
        return poisToSend.sort((poiA, poiB) => poiA.name.localeCompare(poiB.name)).sort((poiA, poiB) => Number(poiB.favorite) - Number(poiA.favorite));
    }

    /**
     * Function that finds a PoI in the storage if it exists by using its coordinates (as strings for URL purposes)
     */
    getPoI(latitude: string, longitude: string): DetailedPoI | undefined {
        return this.storage.find(value => value.latitude.toString() === latitude && value.longitude.toString() === longitude);
    }

    /**
     * Function that filters all PoIs in the storage that are from a given place
     */
    getPoIsOf(place: string) {
        let poisToSend: PoI [] = [];
        this.storage.forEach(detailedPoI => poisToSend.push(this.createPoIAbridged(detailedPoI)))
        return poisToSend.filter(value => value.place === place);
    }

    /**
     * Function that takes in a DetailedPoI and generates an abridged version of it
     */
    createPoIAbridged(detailedPoI: DetailedPoI): PoI {
        let poi: PoI = {
            name: detailedPoI.name,
            place: detailedPoI.place,
            latitude: detailedPoI.latitude,
            longitude: detailedPoI.longitude,
            level: detailedPoI.level,
            type: detailedPoI.type,
            favorite: detailedPoI.favorite
        }
        return poi
    }

    /**
     * Function that triggers the toggle of the 'favorite' attribute of a given PoI
     */
    toggleFavorite(latitude: string, longitude: string): PoI {
        this.storage.find(value => value.latitude.toString() === latitude && value.longitude.toString() === longitude).favorite = 
        !this.storage.find(value => value.latitude.toString() === latitude && value.longitude.toString() === longitude).favorite;
        return this.createPoIAbridged(this.storage.find(value => value.latitude.toString() === latitude && value.longitude.toString() === longitude))
    }

}