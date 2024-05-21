import {
  IconBookOff, IconArrowsDiff, IconAddressBook, IconAperture, IconCopy, IconLayoutDashboard, IconLogin, IconMoodHappy, IconTypography, IconUserPlus, IconBook
} from '@tabler/icons';

import { uniqueId } from 'lodash';

const Menuitems = [
  {
    navlabel: true,
    subheader: 'Libros'
  },
  {
    id: uniqueId(),
    title: 'Listar Libros',
    icon: IconBook,
    href: '/all-books'
  },
  {
    navlabel: true,
    subheader: 'Prestamos'
  },
  {
    id: uniqueId(),
    title: 'Realizar Prestamo',
    icon: IconArrowsDiff,
    href: '/book-loan'
  },
  {
    id: uniqueId(),
    title: 'Devolver Prestamo',
    icon: IconAddressBook,
    href: '/student-loans'
  },
  {
    navlabel: true,
    subheader: 'Estudiantes'
  },
  {
    id: uniqueId(),
    title: 'Estudiantes Sancionados',
    icon: IconBookOff,
    href: '/all-inactive-students'
  }
];

export default Menuitems;
