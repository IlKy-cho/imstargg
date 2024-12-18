import BattleMapEntity from "@/model/entity/BattleMapEntity";
import MessageEntity from "@/model/entity/MessageEntity";
import BrawlStarsImageEntity from "@/model/entity/BrawlStarsImageEntity";
import BattleEventEntity from "@/model/entity/BattleEventEntity";

export default interface BattleMap {
  entity: BattleMapEntity;
  names: MessageEntity[];
  image: BrawlStarsImageEntity | null;
  events: BattleEventEntity[];
}