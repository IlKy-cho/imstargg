import {MessageEntity} from "@/model/entity/MessageEntity";
import {GearEntity} from "@/model/entity/GearEntity";
import { BrawlStarsImageEntity } from "./entity/BrawlStarsImageEntity";

export interface Gear {
  entity: GearEntity;
  names: MessageEntity[];
  image: BrawlStarsImageEntity | null;
}