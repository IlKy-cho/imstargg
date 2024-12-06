import { Menu as MenuIcon, Map as MapIcon, Users as UsersIcon, Trophy as TrophyIcon } from 'lucide-react';
import Link from 'next/link';
import { useSidebar } from './SidebarContext';

interface HeaderProps {
  menuItems: Array<{
    icon: any;
    label: string;
    href: string;
  }>;
}

export default function Header({ menuItems }: HeaderProps) {
  const { toggleSidebar } = useSidebar();

  return (
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
            onClick={toggleSidebar}
            className="md:hidden p-2 hover:bg-[#283593] rounded-lg transition-colors"
          >
            <MenuIcon size={24} />
          </button>
        </div>
      </div>
    </header>
  );
} 