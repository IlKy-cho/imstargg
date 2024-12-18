import BaseEntity from "@/model/entity/BaseEntity";

export default interface StarPowerEntity extends BaseEntity {
  id: number;
  brawlStarsId: string;
  nameMessageCode: string;
  brawlerId: number;
}