"use client";

import {
  NavigationMenu,
  NavigationMenuContent,
  NavigationMenuIndicator,
  NavigationMenuItem,
  NavigationMenuLink,
  NavigationMenuList,
  NavigationMenuTrigger,
  NavigationMenuViewport,
  navigationMenuTriggerStyle,
} from "@/components/ui/navigation-menu";
import {navItems} from "@/config/site";
import Link from "next/link";

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