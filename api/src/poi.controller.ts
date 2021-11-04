import { Controller, Get, Post, Body, Query, Param, Delete, Put } from '@nestjs/common';
import { PoIService } from './poi.service';
import { PoI, PoIAbriged } from './PoI';

@Controller('/PoIs')
export class PoIController {
  constructor(private readonly PoIService: PoIService) {}

  @Get() 
  getPoIs(@Query('place') place): PoIAbriged[] {
      if (place === undefined) {
        return this.PoIService.getAllPoIs();
      } else {
        return this.PoIService.getPoIsOf(place);
      }
  }

  @Post()
  addPoI(@Body() PoI: PoI): PoI {
    this.PoIService.addPoI(PoI);
    return PoI;
  }

  @Get('/:latitude')
  getPoI(@Param('latitude') latitude: number): PoI {
    return this.PoIService.getPoI(latitude);
  }

  /*
  /:longitude
  , @Param('longitude') longitude: number
  @Delete('/:latitude/:longitude')
  deletePoI(@Param('longitude') latitude: number, @Param('latitude') longitude: number): void {
    this.PoIService.deletePoI(latitude, longitude);
  }

  @Put('/:latitude/:longitude')
  favoritePoI(@Param('latitude') latitude: number, @Param('longitude') longitude: number, @Body())  {
    this.PoIService.
  }
  */
}