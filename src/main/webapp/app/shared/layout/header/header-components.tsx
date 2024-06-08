import React from 'react';
import { Translate } from 'react-jhipster';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChartLine, faFileContract, faFileLines } from '@fortawesome/free-solid-svg-icons';

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/mcit-logo.png" alt="Logo" /> &nbsp;
  </div>
);

export const Brand = () => (
  <NavbarBrand className="brand-logo">
    <BrandIcon />
    <span className="brand-title">
      <Translate contentKey="global.title">NTA Employee's Database</Translate>
    </span>
    {/* <span className="navbar-version">{VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`}</span> */}
  </NavbarBrand>
);
