import { Injectable, OnModuleInit } from '@nestjs/common';
import { promises } from 'fs';
import { PoI, APIPoI } from './PoI';
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
          .get<APIPoI[]>('https://api.npoint.io/01b169adea931451edce')
          .pipe(
            map((response) =>
              response.data.map((apiPoI) => ({
                name : apiPoI.Name,
                place: apiPoI.Lieu,
                longitude: Number(apiPoI.GPS_Coord.split(",")[0]),
                latitude: Number(apiPoI.GPS_Coord.split(",")[1]),
                badge: apiPoI.Badge === "Oui" ? true : false,
                commentary: apiPoI.Commentaires,
                level: apiPoI.Niveau,
                type: apiPoI.Type,
                picture_link: apiPoI.gx_media_links,
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

    getPoI(longitude: number, latitude: number): PoI | undefined {
        return this.storage.find(value => value.longitude === longitude && value.latitude === latitude);
    }


    getPoIsOf(place: string) {
        return this.storage.filter(value => value.place === place);
    }

    deletePoI(longitude: number, latitude: number): void {
        let poi: PoI = this.getPoI(longitude, latitude);
        if (poi !== undefined) {
            let poiIndex: number = this.storage.indexOf(poi);
            if (poiIndex > -1) {
                this.storage.splice(poiIndex, 1)
            }
        }
    }

}
  