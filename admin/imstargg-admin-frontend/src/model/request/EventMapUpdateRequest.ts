import {Language} from "@/model/enums/Language";

export interface EventMapUpdateRequest {
  names: Record<Language, string>;
}