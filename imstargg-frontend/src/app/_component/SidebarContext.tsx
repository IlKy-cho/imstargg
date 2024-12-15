"use client";

import { createContext, useContext, useState, ReactNode } from 'react';

interface SidebarContextType {
  sidebarOpened: boolean;
  setSidebarOpened: (opened: boolean) => void;
  toggleSidebar: () => void;
}

const SidebarContext = createContext<SidebarContextType | undefined>(undefined);

export function SidebarProvider({ children }: { children: ReactNode }) {
  const [sidebarOpened, setSidebarOpened] = useState(false);

  const toggleSidebar = () => setSidebarOpened(prev => !prev);

  return (
    <SidebarContext.Provider value={{ sidebarOpened, setSidebarOpened, toggleSidebar }}>
      {children}
    </SidebarContext.Provider>
  );
}

export function useSidebar() {
  const context = useContext(SidebarContext);
  if (context === undefined) {
    throw new Error('useSidebar must be used within a SidebarProvider');
  }
  return context;
} 