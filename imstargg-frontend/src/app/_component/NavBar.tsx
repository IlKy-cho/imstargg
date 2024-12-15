"use client";

import { Map as MapIcon, Users as UsersIcon, Trophy as TrophyIcon } from 'lucide-react';
import Header from './Header';
import Sidebar from './Sidebar';
import { SidebarProvider } from './SidebarContext';

export default function NavBar() {
  const menuItems = [
    { icon: MapIcon, label: '맵', href: '/maps' },
    { icon: UsersIcon, label: '브롤러', href: '/brawlers' },
    { icon: TrophyIcon, label: '경쟁전', href: '/competitive' },
  ];

  return (
    <SidebarProvider>
      <Header menuItems={menuItems} />
      <Sidebar menuItems={menuItems} />
    </SidebarProvider>
  );
}