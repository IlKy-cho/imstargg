import BattleMapEntity from "@/model/entity/BattleMapEntity";
import MessageEntity from "@/model/entity/MessageEntity";
import BrawlStarsImageEntity from "@/model/entity/BrawlStarsImageEntity";

export default interface BattleEventMap {
  entity: BattleMapEntity;
  names: MessageEntity[];
  image: BrawlStarsImageEntity | null;
}