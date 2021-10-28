import { Controller, Get, Post, Body, Query, Param, Delete, Put } from '@nestjs/common';
import { PoIService } from './poi.service';
import { PoI } from './PoI';

@Controller('/PoIs')
export class PoIController {
  constructor(private readonly PoIService: PoIService) {}

  @Get() 
  getPoIs(@Query('place') place): PoI[] {
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

  @Get('/:longitude/:latitude')
  getPoI(@Param('longitude') longitude: number, @Param('latitude') latitude: number): PoI {
    return this.PoIService.getPoI(longitude, latitude);
  }

  @Delete('/:longitude/:latitude')
  deletePoI(@Param('longitude') longitude: number, @Param('latitude') latitude: number): void {
    this.PoIService.deletePoI(longitude, latitude);
  }

  /*
  @Put('/:longitude/:latitude')
  favoritePoI(@Param('longitude') longitude: number, @Param('latitude') latitude: number, @Body())  {
    this.PoIService.
  }
  */
}