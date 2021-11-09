import { Controller, Get, Post, Body, Query, Param, Delete, Put } from '@nestjs/common';
import { PoIService } from './poi.service';
import { PoI, DetailedPoI } from './PoI';

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
  addPoI(@Body() detailedPoI: DetailedPoI): DetailedPoI {
    this.PoIService.addPoI(detailedPoI);
    return detailedPoI;
  }

  @Get('/:latitude/:longitude')
  getPoI(@Param('latitude') latitude: string, @Param('longitude') longitude: string): DetailedPoI {
    return this.PoIService.getPoI(latitude, longitude);
  }

  /*
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