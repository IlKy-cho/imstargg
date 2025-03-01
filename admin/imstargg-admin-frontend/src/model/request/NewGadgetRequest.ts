import {Language} from "@/model/enums/Language";

export interface NewGadgetRequest {
  brawlStarsId: number;
  names: Record<Language, string>;
}