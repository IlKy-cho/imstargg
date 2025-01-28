import {Brawler} from "@/model/Brawler";
import ImStarGgLogo from "@/../public/logo.png"

export const meta = {
  name: "ImStarGG",
  logo: ImStarGgLogo,
  email: "imstargg2024@gmail.com",
} as const;

export const metadataTitle = (title: string) => `${title} | ${meta.name}`;

export const battleEventHref = (id: number) => `/events/${id}`;

export const brawlerHref = (brawler: Brawler) => `/brawlers/${brawler.id}`;

export const playerHref = (tag: string) => `/players/${encodeURIComponent(tag)}`;
