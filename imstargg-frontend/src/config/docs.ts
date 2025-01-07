import {Brawler} from "@/model/Brawler";

export const navItems = [
  { label: '브롤러', href: '/brawlers' },
  { label: '이벤트', href: '/events' },
];

export const brawlerHref = (brawler: Brawler) => `/brawlers/${brawler.id}`;