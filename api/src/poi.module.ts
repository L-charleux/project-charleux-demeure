import { Module } from '@nestjs/common';
import { PoIController } from './poi.controller';
import { PoIService } from './poi.service';
import { HttpModule } from '@nestjs/axios';

@Module({
  imports: [HttpModule],
  controllers: [PoIController],
  providers: [PoIService],
})
export class PoIModule {}
