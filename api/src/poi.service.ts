import { Injectable, OnModuleInit } from '@nestjs/common';
import { PoI, DetailedPoI, ExternalPoI} from './PoI';
import { HttpService } from '@nestjs/axios';
import { firstValueFrom, map } from 'rxjs';

@Injectable()
export class PoIService implements OnModuleInit {

    private storage: DetailedPoI[] = [];

    constructor(private readonly httpService: HttpService) {}

    async onModuleInit() {
       /*
       const file = await promises.readFile('src/dataset.json');
       const dataParsed: APIPoI[] = JSON.parse(file.toString());
       dataParsed.forEach(element => { this.addAPIPoI(element) });
       */

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
                favorite: "False"
              })),
            ),
          ),
      );
        apiPoIs.forEach(poi => {this.addPoI(poi)});
    }

    addPoI(detailedPoI: DetailedPoI): void {
        if (!(this.storage.some(value => value === detailedPoI))) {
            this.storage.push(detailedPoI)
        }
    }

    getAllPoIs() {
        let poisToSend: PoI [] = [];
        this.storage.forEach(detailedPoI => poisToSend.push(this.createPoIAbriged(detailedPoI)))
        return poisToSend.sort((poiA, poiB) => poiA.name.localeCompare(poiB.name));
    }

    getPoI(latitude: string, longitude: string): DetailedPoI | undefined {
        return this.storage.find(value => value.latitude.toString() === latitude && value.longitude.toString() === longitude);
    }

    getPoIsOf(place: string) {
        let poisToSend: PoI [] = [];
        this.storage.forEach(detailedPoI => poisToSend.push(this.createPoIAbriged(detailedPoI)))
        return poisToSend.filter(value => value.place === place);
    }

    /*
    deletePoI(latitude: number, longitude: number): void {
        let poi: PoI = this.getPoI(latitude, longitude);
        if (poi !== undefined) {
            let poiIndex: number = this.storage.indexOf(poi);
            if (poiIndex > -1) {
                this.storage.splice(poiIndex, 1)
            }
        }
    }
    */

    createPoIAbriged(detailedPoI: DetailedPoI): PoI {
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

}
  