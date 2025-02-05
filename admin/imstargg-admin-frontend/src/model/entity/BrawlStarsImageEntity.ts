import {BaseEntity} from "@/model/entity/BaseEntity";

export interface BrawlStarsImageEntity extends BaseEntity {
  id: number;
  type: string;
  code: string;
  storedName: string;
}