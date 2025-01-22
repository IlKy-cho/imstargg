import {Brawler} from "@/model/Brawler";
import {fetchGetBrawler, fetchGetBrawlers} from "@/lib/api/api";
import {ListResponse} from "@/model/response/ListResponse";
import {ApiError} from "@/model/response/error";
import { BrawlerRole } from "@/model/enums/BrawlerRole";
import { BrawlerRarity } from "@/model/enums/BrawlerRarity";
import { Gadget } from "@/model/Gadget";
import { Gear } from "@/model/Gear";
import { StarPower } from "@/model/StarPower";

interface BrawlerResponse {
  id: number;
  name: string;
  rarity: BrawlerRarity;
  role: BrawlerRole;
  gadgets: Gadget[];
  gears: Gear[];
  starPowers: StarPower[];
  imagePath: string | null;
}

export async function getBrawlers() : Promise<Brawler[]> {
  const response = await fetchGetBrawlers({revalidate: 60 * 60});

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlerResponse>;
    return data.content
      .map(brawler => ({
        ...brawler,
        imageUrl: brawler.imagePath ? new URL(brawler.imagePath, process.env.NEXT_PUBLIC_IMAGE_BASE_URL).toString() : null
      }));
  }

  throw await ApiError.create(response);
}

export async function getBrawler(id: number) : Promise<Brawler | null> {
  const response = await fetchGetBrawler(id, {revalidate: 60 * 60});

  if (response.ok) {
    const data = await response.json() as BrawlerResponse;
    return {
      ...data,
      imageUrl: data.imagePath ? new URL(data.imagePath, process.env.NEXT_PUBLIC_IMAGE_BASE_URL).toString() : null
    };
  } else if (response.status === 404) {
    return null;
  }

  throw await ApiError.create(response);
}
