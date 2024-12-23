"use client";

import {
  NavigationMenu,
  NavigationMenuItem,
  NavigationMenuLink,
  NavigationMenuList,
  navigationMenuTriggerStyle,
} from "@/components/ui/navigation-menu";
import {navItems} from "@/config/site";

export default function MainNav() {
  return (
    <NavigationMenu>
      <NavigationMenuList className="flex">
        <NavigationMenuItem>
          {navItems.map((item) => {
            return (
              <NavigationMenuLink 
                key={item.href}
                href={item.href}
                className={`${navigationMenuTriggerStyle()} bg-background`}
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