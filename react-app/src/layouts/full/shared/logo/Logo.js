import { Link } from 'react-router-dom';
import LogoDark from 'src/assets/images/logos/usac-logo-mejor.png';
import { styled } from '@mui/material';

const LinkStyled = styled(Link)(() => ({
  height: '90px',
  width: '225px',
  overflow: 'hidden',
  display: 'block',
  alignContent: 'center',
  alignItems: 'center'
}));

const Logo = () => {
  return (
    <LinkStyled to="/">
      <img src={LogoDark} width="100%" />
    </LinkStyled>
  )
};

export default Logo;
