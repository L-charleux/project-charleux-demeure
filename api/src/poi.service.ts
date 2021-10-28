import { Injectable, OnModuleInit } from '@nestjs/common';
import { promises } from 'fs';
import { PoI, ExternalPoI } from './PoI';
import { HttpService } from '@nestjs/axios';
import { firstValueFrom, map } from 'rxjs';

@Injectable()
export class PoIService implements OnModuleInit {

    private storage: PoI[] = [];

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
                badge: apiPoI.Badge === "Oui" ? true : false,
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

    /*
    addAPIPoI(apiPoI: APIPoI): void {
        let poi: PoI = {name: apiPoI.Name, 
                        place: apiPoI.Name, 
                        longitude: Number(apiPoI.GPS_Coord.split(",")[0]), 
                        latitude: Number(apiPoI.GPS_Coord.split(",")[1]), 
                        badge: apiPoI.Badge === "Oui" ? true : false,
                        commentary: apiPoI.Commentaires, 
                        level: apiPoI.Niveau, 
                        type: apiPoI.Type, 
                        picture_link: apiPoI.gx_media_links};
        this.addPoI(poi);
    }
    */

    addPoI(poi: PoI): void {
        if (!(this.storage.some(value => value === poi))) {
            this.storage.push(poi)
        }
    }

    getAllPoIs() {
        return this.storage.sort((poiA, poiB) => poiA.name.localeCompare(poiB.name));
    }

    getPoI(latitude: number, longitude: number): PoI | undefined {
        return this.storage.find(value => value.latitude === latitude && value.longitude === longitude);
    }


    getPoIsOf(place: string) {
        return this.storage.filter(value => value.place === place);
    }

    deletePoI(latitude: number, longitude: number): void {
        let poi: PoI = this.getPoI(latitude, longitude);
        if (poi !== undefined) {
            let poiIndex: number = this.storage.indexOf(poi);
            if (poiIndex > -1) {
                this.storage.splice(poiIndex, 1)
            }
        }
    }

}
  