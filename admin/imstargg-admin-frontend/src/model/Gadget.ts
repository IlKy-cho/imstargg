import {GadgetEntity} from "@/model/entity/GadgetEntity";
import {MessageEntity} from "@/model/entity/MessageEntity";
import { BrawlStarsImageEntity } from "./entity/BrawlStarsImageEntity";

export interface Gadget {
  entity: GadgetEntity;
  names: MessageEntity[];
  image: BrawlStarsImageEntity | null;
}