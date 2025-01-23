import {Brawler} from "@/model/Brawler";

export const meta = {
  name: "ImStarGG",
} as const;

export const metadataTitle = (title: string) => `${title} | ${meta.name}`;

export const battleEventHref = (id: number) => `/events/${id}`;

export const brawlerHref = (brawler: Brawler) => `/brawlers/${brawler.id}`;
