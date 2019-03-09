import { NbMenuItem } from '@nebular/theme';

export const MENU_ITEMS: NbMenuItem[] = [
  {
    title: 'Dashboard',
    icon: 'nb-home',
    link: '/pages/dashboard',
    home: true,
  },
  {
    title: 'MONITORING',
    group: true,
  },
  {
    title: 'Maps',
    icon: 'nb-location',
    children: [

      {
        title: 'Bus Live Tracking',
        link: '/pages/maps/leaflet',
      },
    ],
  },
];
