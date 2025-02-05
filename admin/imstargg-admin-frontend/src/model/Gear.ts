import {MessageEntity} from "@/model/entity/MessageEntity";
import {GearEntity} from "@/model/entity/GearEntity";

export interface Gear {
  entity: GearEntity;
  names: MessageEntity[];
}