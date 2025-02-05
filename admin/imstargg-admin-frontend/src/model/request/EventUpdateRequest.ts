import {Language} from "@/model/enums/Language";

export interface EventUpdateRequest {
  names: Record<Language, string>;
}