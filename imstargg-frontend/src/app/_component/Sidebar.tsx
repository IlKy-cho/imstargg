import Link from 'next/link';
import { useSidebar } from './SidebarContext';

interface SidebarProps {
  menuItems: Array<{
    icon: any;
    label: string;
    href: string;
  }>;
}

export default function Sidebar({ menuItems }: SidebarProps) {
  const { sidebarOpened, setSidebarOpened } = useSidebar();

  return (
    <>
      {sidebarOpened && (
        <div
          className="fixed inset-0 bg-black/50 z-40 md:hidden"
          onClick={() => setSidebarOpened(false)}
        />
      )}

      <div
        className={`absolute top-14 right-0 h-[calc(100vh-3.5rem)] w-64 bg-white shadow-lg transform transition-transform duration-300 ease-in-out z-50 md:hidden ${
          sidebarOpened ? 'translate-x-0' : 'translate-x-full'
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