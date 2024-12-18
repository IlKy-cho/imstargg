import Link from "next/link";
import {
  NavigationMenu,
  NavigationMenuLink,
  NavigationMenuList,
  navigationMenuTriggerStyle
} from "@/components/ui/navigation-menu";
import Image from 'next/image';
import {menuItems} from "@/config/site";

export default function SiteHeader() {
  return (
    <header className="bg-gray-400 h-14 w-full">
      <div className="flex items-center h-full mx-auto gap-6">
        <Link href="/" className="flex items-center gap-2 hover:opacity-80 transition-opacity">
          <Image src="/logo.png" alt="ImStarGG 로고" width={30} height={30} />
          <h1 className="text-xl font-bold">아임스타지지 어드민</h1>
        </Link>
        <NavigationMenu>
          <NavigationMenuList>
            {menuItems.map((item) => {
              const Icon = item.icon;
              return (
                <Link key={item.href} href={item.href} legacyBehavior passHref>
                  <NavigationMenuLink className={navigationMenuTriggerStyle()}>
                    <Icon className="mr-2 h-4 w-4" />
                    {item.label}
                  </NavigationMenuLink>
                </Link>
              );
            })}
          </NavigationMenuList>
        </NavigationMenu>
      </div>
    </header>
  );
}