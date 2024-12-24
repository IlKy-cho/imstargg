"use client";

import {
  NavigationMenu,
  NavigationMenuItem,
  NavigationMenuLink,
  NavigationMenuList,
  navigationMenuTriggerStyle,
} from "@/components/ui/navigation-menu";
import {navItems} from "@/config/docs";

export default function MainNav() {
  return (
    <NavigationMenu>
      <div className="flex gap-5">
        <NavigationMenuList>
          <NavigationMenuItem>
            {navItems.map((item) => {
              return (
                <NavigationMenuLink
                  key={item.href}
                  href={item.href}
                  className={`${navigationMenuTriggerStyle()} bg-zinc-900 text-white hover:bg-zinc-500`}
                >
                  {item.label}
                </NavigationMenuLink>
              );
            })}
          </NavigationMenuItem>
        </NavigationMenuList>
      </div>
    </NavigationMenu>
  );
}