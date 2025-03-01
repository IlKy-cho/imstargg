import {Language} from "@/model/enums/Language";

export interface NewStarPowerRequest {
  brawlStarsId: number;
  names: Record<Language, string>;
}