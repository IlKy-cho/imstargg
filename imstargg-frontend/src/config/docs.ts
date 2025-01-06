import {Brawler} from "@/model/Brawler";

export const navItems = [
  { label: '브롤러', href: '/brawlers' },
  { label: '맵', href: '/maps' },
];

export const brawlerHref = (brawler: Brawler) => `/brawlers/${brawler.id}`;