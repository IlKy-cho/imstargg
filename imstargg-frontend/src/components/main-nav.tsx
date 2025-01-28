"use client";

import {
  NavigationMenu,
  NavigationMenuItem,
  NavigationMenuLink,
  NavigationMenuList,
} from "@/components/ui/navigation-menu";
import {navItems} from "@/config/docs";

export default function MainNav() {
  return (
    <NavigationMenu>
      <NavigationMenuList>
        <NavigationMenuItem className="flex gap-4">
          {navItems.map((item) => {
            return (
              <NavigationMenuLink
                key={item.href}
                href={item.href}
                className="font-medium text-white hover:text-zinc-400 focus:text-zinc-400"
              >
                {item.label}
              </NavigationMenuLink>
            );
          })}
        </NavigationMenuItem>
      </NavigationMenuList>
    </NavigationMenu>
  );
}