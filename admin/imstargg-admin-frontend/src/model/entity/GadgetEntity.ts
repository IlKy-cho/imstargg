import BaseEntity from "@/model/entity/BaseEntity";

export default interface GadgetEntity extends BaseEntity {
  id: number;
  brawlStarsId: string;
  nameMessageCode: string;
  brawlerId: number;
}