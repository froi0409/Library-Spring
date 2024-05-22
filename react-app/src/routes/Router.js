import React, { lazy } from 'react';
import { Navigate } from 'react-router-dom';
import Loadable from '../layouts/full/shared/loadable/Loadable';
import { element } from 'prop-types';
import BookLoan from 'src/views/librarian/components/BookLoan';
import BookListTable from 'src/views/book/BooksListTable';
import StudentLayout from 'src/layouts/user/StudentLayout';
import StudentBookLoansTable from 'src/views/librarian/components/StudentBookLoansTable';
import InactiveStudentsTable from 'src/views/librarian/components/InactiveSudentsTable';
import TodayLoansToDue from 'src/views/librarian/reports/TodayLoansToDue';
import OverdueLoans from 'src/views/librarian/reports/OverdueLoans';
import RevenueInformation from 'src/views/librarian/reports/RevenueInformation';
import DegreeTopLoans from 'src/views/librarian/reports/DegreeTopLoans';
import OverduePaymentByStudent from 'src/views/librarian/reports/OverduePaymemtByStudent';

/* ***Layouts**** */
const FullLayout = Loadable(lazy(() => import('../layouts/full/FullLayout')));
const BlankLayout = Loadable(lazy(() => import('../layouts/blank/BlankLayout')));

/* ****Pages***** */
const Dashboard = Loadable(lazy(() => import('../views/dashboard/Dashboard')))
const SamplePage = Loadable(lazy(() => import('../views/sample-page/SamplePage')))
const Icons = Loadable(lazy(() => import('../views/icons/Icons')))
const TypographyPage = Loadable(lazy(() => import('../views/utilities/TypographyPage')))
const Shadow = Loadable(lazy(() => import('../views/utilities/Shadow')))
const Error = Loadable(lazy(() => import('../views/authentication/Error')));
const Register = Loadable(lazy(() => import('../views/authentication/Register')));
const Login = Loadable(lazy(() => import('../views/authentication/Login')));

const Router = [
  {
    path: '/',
    element: <FullLayout />,
    children: [
      { path: '/', element: <Navigate to="/auth/login" /> },
      { path: '/dashboard', exact: true, element: <Dashboard /> },
      { path: '/sample-page', exact: true, element: <SamplePage /> },
      { path: '/icons', exact: true, element: <Icons /> },
      { path: '/ui/typography', exact: true, element: <TypographyPage /> },
      { path: '/ui/shadow', exact: true, element: <Shadow /> },
      { path: '*', element: <Navigate to="/auth/404" /> },
      { path: '/book-loan', element: <BookLoan /> },
      { path: '/all-books', element: <BookListTable /> },
      { path: '/student-loans', element: <StudentBookLoansTable /> },
      { path: '/all-inactive-students', element: <InactiveStudentsTable /> },
      { path: '/today-loans-to-due', element: <TodayLoansToDue /> },
      { path: '/overdue-loans', element: <OverdueLoans /> },
      { path: '/revenue-information', element: <RevenueInformation /> },
      { path: '/degree-top-loans', element: <DegreeTopLoans /> },
      { path: '/overdue-payment-by-student', element: <OverduePaymentByStudent /> }
    ],
  },
  {
    path: '/auth',
    element: <BlankLayout />,
    children: [
      { path: '404', element: <Error /> },
      { path: '/auth/register', element: <Register /> },
      { path: '/auth/login', element: <Login /> },
      { path: '*', element: <Navigate to="/auth/404" /> },
    ],
  },
  {
    path: '/student',
    element: <StudentLayout />,
    children: [
      { path: '/student/dashboard', element: <Dashboard /> }
    ]
  }
];

export default Router;
