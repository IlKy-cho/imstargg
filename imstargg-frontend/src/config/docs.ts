import supercellLogo from '@/../public/supercell-logo.svg';
import youtubeLogo from '@/../public/youtube-logo.png';
import xLogo from '@/../public/x-logo.jpg';
import naverCafeLogo from '@/../public/naver-cafe-logo.webp';


export const navItems = [
  { label: '플레이어', href: '/players' },
  { label: '브롤러', href: '/brawlers' },
  { label: '이벤트', href: '/events' },
];

export const policyItems = {
  agreement: { label: '이용약관', href: '/policies/agreement' }
} as const;

export type NewsChannelItem = {
  label: string;
  href: string;
  icon: string;
}

export const newsChannelItems: NewsChannelItem[] = [
  { label: '슈퍼셀', href: 'https://supercell.com/en/games/brawlstars/ko/blog/', icon: supercellLogo },
  { label: '유튜브', href: 'https://www.youtube.com/brawlstarskorea', icon: youtubeLogo },
  { label: '네이버 카페', href: 'https://cafe.naver.com/brawlstars', icon: naverCafeLogo },
  { label: 'X', href: 'https://x.com/brawlstars', icon: xLogo },
];