import { NestFactory } from '@nestjs/core';
import { PoIModule } from './poi.module';

async function bootstrap() {
  const app = await NestFactory.create(PoIModule);
  const port = process.env.PORT??3000;
  console.log(port);
  await app.listen(port);
}
bootstrap();
