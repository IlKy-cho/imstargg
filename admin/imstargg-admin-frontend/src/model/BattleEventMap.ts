import MessageEntity from "@/model/entity/MessageEntity";
import BrawlStarsImageEntity from "@/model/entity/BrawlStarsImageEntity";

export default interface BattleEventMap {
  names: MessageEntity[];
  image: BrawlStarsImageEntity | null;
}