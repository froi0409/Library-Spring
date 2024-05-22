import {
  IconClipboardText, IconBookOff, IconArrowsDiff, IconAddressBook, IconAperture, IconCopy, IconLayoutDashboard, IconLogin, IconMoodHappy, IconTypography, IconUserPlus, IconBook
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
    id: uniqueId(),
    title: 'Prestamos de Hoy',
    icon: IconClipboardText,
    href: '/today-loans-to-due'
  },
  {
    id: uniqueId(),
    title: 'Prestamos Atrasados',
    icon: IconClipboardText,
    href: '/overdue-loans'
  },
  {
    id: uniqueId(),
    title: 'Ganancias por Prestamo',
    icon: IconClipboardText,
    href: '/revenue-information'
  },
  {
    id: uniqueId(),
    title: 'Top Carreras',
    icon: IconClipboardText,
    href: '/degree-top-loans'
  },
  {
    id: uniqueId(),
    title: 'Moras Pagadas',
    icon: IconClipboardText,
    href: '/overdue-payment-by-student'
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
