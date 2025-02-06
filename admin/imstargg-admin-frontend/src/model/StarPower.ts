import {MessageEntity} from "@/model/entity/MessageEntity";
import {StarPowerEntity} from "@/model/entity/StarPowerEntity";
import { BrawlStarsImageEntity } from "./entity/BrawlStarsImageEntity";

export interface StarPower {
  entity: StarPowerEntity;
  names: MessageEntity[];
  image: BrawlStarsImageEntity | null;
}