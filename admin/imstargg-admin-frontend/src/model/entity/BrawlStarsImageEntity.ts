import BaseEntity from "@/model/entity/BaseEntity";

export default interface BrawlStarsImageEntity extends BaseEntity {
  id: number;
  type: string;
  code: string;
  storedName: string;
  url: string;
}