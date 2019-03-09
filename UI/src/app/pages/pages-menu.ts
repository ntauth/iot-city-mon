import { NbMenuItem } from '@nebular/theme';

export const MENU_ITEMS: NbMenuItem[] = [
  {
    title: 'Dashboard',
    icon: 'nb-home',
    link: '/pages/dashboard',
    home: true,
  },
  {
    title: 'FEATURES',
    group: true,
  },
  {
    title: 'Maps',
    icon: 'nb-location',
    children: [

      {
        title: 'Leaflet Maps',
        link: '/pages/maps/leaflet',
      },
    ],
  },
];
