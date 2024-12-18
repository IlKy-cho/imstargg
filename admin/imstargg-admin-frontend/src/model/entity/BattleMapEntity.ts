import BaseEntity from "@/model/entity/BaseEntity";

export default interface BattleMapEntity extends BaseEntity {
  id: number;
  code: string;
  nameMessageCode: string;
}