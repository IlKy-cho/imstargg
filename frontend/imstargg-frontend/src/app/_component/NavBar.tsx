"use client";

import { Menu as MenuIcon, Map as MapIcon, Users as UsersIcon, Trophy as TrophyIcon } from 'lucide-react';
import Link from 'next/link';
import { useState } from 'react';

export default function NavBar() {
  const [sidebarOpened, setSidebarOpened] = useState(false);

  const menuItems = [
    { icon: MapIcon, label: '맵', href: '/maps' },
    { icon: UsersIcon, label: '브롤러', href: '/brawlers' },
    { icon: TrophyIcon, label: '경쟁전', href: '/competitive' },
  ];

  return (
    <>
      <header className="bg-[#1a237e] text-white h-14 px-4 w-full z-50">
        <div className="max-w-7xl mx-auto h-full flex items-center">
          <div className="flex items-center gap-6">
            <div className="flex items-center gap-2">
              <img src="/logo.png" alt="ImStarGG" className="h-8 w-8" />
              <h1 className="text-xl font-bold">ImStarGG</h1>
            </div>

            <nav className="hidden md:flex items-center gap-4">
              {menuItems.map((item) => {
                const IconComponent = item.icon;
                return (
                  <Link
                    key={item.href}
                    href={item.href}
                    className="flex items-center gap-2 p-2 hover:bg-[#283593] rounded-lg transition-colors"
                  >
                    <IconComponent size={20} />
                    <span>{item.label}</span>
                  </Link>
                );
              })}
            </nav>
          </div>


          <div className="ml-auto">
            <button
              onClick={() => setSidebarOpened(!sidebarOpened)}
              className="md:hidden p-2 hover:bg-[#283593] rounded-lg transition-colors"
            >
              <MenuIcon size={24} />
            </button>
          </div>
        </div>
      </header>


      {sidebarOpened && (
        <div
          className="fixed inset-0 bg-black/50 z-40 md:hidden"
          onClick={() => setSidebarOpened(false)}
        />
      )}

      <div
        className={`absolute top-14 right-0 h-[calc(100vh-3.5rem)] w-64 bg-white shadow-lg transform transition-transform duration-300 ease-in-out z-50 md:hidden ${sidebarOpened ? 'translate-x-0' : 'translate-x-full'
          }`}
      >
        <nav className="p-4">
          <ul className="space-y-2">
            {menuItems.map((item) => {
              const IconComponent = item.icon;
              return (
                <li key={item.href}>
                  <Link
                    href={item.href}
                    className="flex items-center gap-3 p-3 text-gray-700 hover:bg-gray-100 rounded-lg transition-colors"
                    onClick={() => setSidebarOpened(false)}
                  >
                    <IconComponent size={20} />
                    <span>{item.label}</span>
                  </Link>
                </li>
              );
            })}
          </ul>
        </nav>
      </div>
    </>
  );
}