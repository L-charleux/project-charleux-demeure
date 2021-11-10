import { Controller, Get, Post, Body, Query, Param, Delete, Put } from '@nestjs/common';
import { PoIService } from './poi.service';
import { PoI, DetailedPoI } from './PoI';

@Controller('/PoIs')
export class PoIController {
  constructor(private readonly PoIService: PoIService) {}

  /**
   * Function that calls every PoI of a given place if the query 'place' was used through PoIService.getPoIOf(place)
   * Else it calls every PoI using PoIService.getAllPoIs()
   * Only the later is used in the application
   */
  @Get() 
  getPoIs(@Query('place') place): PoI[] {
      if (place === undefined) {
        return this.PoIService.getAllPoIs();
      } else {
        return this.PoIService.getPoIsOf(place);
      }
  }

  /**
   * Function that creates a PoI, it is not used in the application
   */
  @Post()
  addPoI(@Body() detailedPoI: DetailedPoI): DetailedPoI {
    this.PoIService.addPoI(detailedPoI);
    return detailedPoI;
  }

  /**
  * Function that gets a PoI using its identifiers (its coordinates) with PoIService.getPoI
  */
  @Get('/:latitude/:longitude')
  getPoI(@Param('latitude') latitude: string, @Param('longitude') longitude: string): DetailedPoI {
    return this.PoIService.getPoI(latitude, longitude);
  }
  
  /**
   * Function that toggles the parameter 'favorite' of a given PoI through PoIService.toggleFavorite(latitude, longitude)
   */
  @Put('/:latitude/:longitude')
  putFavoritePoI(@Param('latitude') latitude: string, @Param('longitude') longitude: string): PoI  {
    return this.PoIService.toggleFavorite(latitude, longitude);
  }

}