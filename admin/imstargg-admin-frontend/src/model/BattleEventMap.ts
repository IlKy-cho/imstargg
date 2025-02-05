import {MessageEntity} from "@/model/entity/MessageEntity";
import {BrawlStarsImageEntity} from "@/model/entity/BrawlStarsImageEntity";

export interface BattleEventMap {
  names: MessageEntity[];
  image: BrawlStarsImageEntity | null;
}