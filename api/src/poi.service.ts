import { Injectable, OnModuleInit } from '@nestjs/common';
import { promises } from 'fs';
import { PoI, ExternalPoI, PoIAbriged } from './PoI';
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

    addPoI(poi: PoI): void {
        if (!(this.storage.some(value => value === poi))) {
            this.storage.push(poi)
        }
    }

    getAllPoIs() {
        let poisToSend: PoIAbriged [] = [];
        this.storage.forEach(poi => poisToSend.push(this.createPoIAbriged(poi)))
        return poisToSend.sort((poiA, poiB) => poiA.name.localeCompare(poiB.name));
    }

    getPoI(latitude: number, longitude: number): PoI | undefined {
        return this.storage.find(value => value.latitude === latitude && value.longitude === longitude);
    }

    getPoIsOf(place: string) {
        let poisToSend: PoIAbriged [] = [];
        this.storage.forEach(poi => poisToSend.push(this.createPoIAbriged(poi)))
        return poisToSend.filter(value => value.place === place);
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

    createPoIAbriged(poi: PoI): PoIAbriged {
        let poiAbriged: PoIAbriged = {
            name: poi.name,
            place: poi.place,
            latitude: poi.latitude,
            longitude: poi.longitude,
            level: poi.level,
            type: poi.type,
            favorite: poi.favorite
        }
        return poiAbriged
    }

}
  