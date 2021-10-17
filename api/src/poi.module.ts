import { Module } from '@nestjs/common';
import { PoIController } from './poi.controller';
import { PoIService } from './poi.service';

@Module({
  imports: [],
  controllers: [PoIController],
  providers: [PoIService],
})
export class PoIModule {}
