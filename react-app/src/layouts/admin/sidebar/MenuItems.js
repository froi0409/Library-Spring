import {
  IconAperture, IconCopy, IconFileUpload, IconLayoutDashboard, IconLogin, IconMoodHappy, IconTypography, IconUserPlus, IconBook
} from '@tabler/icons';

import { uniqueId } from 'lodash';

const Menuitems = [
  {
    navlabel: true,
    subheader: 'Gestion de Datos',
  },
  {
    id: uniqueId(),
    title: 'Archivo de Entrada',
    icon: IconFileUpload,
    href: '/admin/upload-datafile',
  },
  {
    navlabel: true,
    subheader: 'Carreras',
  }
  
];

export default Menuitems;
