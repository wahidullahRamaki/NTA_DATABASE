//
import './header.scss';

import React, { useState, useEffect } from 'react';
import { Translate, Storage } from 'react-jhipster';
import { Navbar, Nav, NavbarToggler, Collapse, NavItem, NavLink } from 'reactstrap';
import { NavLink as RouterNavLink } from 'react-router-dom';
import LoadingBar from 'react-redux-loading-bar';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUsers, faTable } from '@fortawesome/free-solid-svg-icons';

import { isRTL } from 'app/config/translation';
import { Brand } from './header-components';
import { AccountMenu, LocaleMenu } from '../menus';
import { useAppDispatch } from 'app/config/store';
import { setLocale } from 'app/shared/reducers/locale';

export interface IHeaderProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  ribbonEnv: string;
  isInProduction: boolean;
  isOpenAPIEnabled: boolean;
  currentLocale: string;
}

const Header = (props: IHeaderProps) => {
  const [menuOpen, setMenuOpen] = useState(false);

  useEffect(() => {
    document.querySelector('html').setAttribute('dir', isRTL(Storage.session.get('locale')) ? 'rtl' : 'ltr');
  }, [props.currentLocale]);

  const dispatch = useAppDispatch();

  const handleLocaleChange = event => {
    const langKey = event.target.value;
    Storage.session.set('locale', langKey);
    dispatch(setLocale(langKey));
    document.querySelector('html').setAttribute('dir', isRTL(langKey) ? 'rtl' : 'ltr');
  };

  const toggleMenu = () => setMenuOpen(!menuOpen);

  return (
    <div id="app-header">
      <LoadingBar className="loading-bar" />
      <Navbar data-cy="navbar" dark expand="md" fixed="top" className="bg-primary">
        <NavbarToggler aria-label="Menu" onClick={toggleMenu} />
        <Brand />
        <Collapse isOpen={menuOpen} navbar>
          <Nav id="header-tabs" className="ms-auto" navbar>
            {props.isAuthenticated && (
              <NavItem>
                <NavLink tag={RouterNavLink} to="/nta-table" className="d-flex align-items-center">
                  <FontAwesomeIcon icon={faTable} />
                  <span>
                    <Translate contentKey="global.menu.entities.ntaTable">NTA Table</Translate>
                  </span>
                </NavLink>
              </NavItem>
            )}
            {props.isAuthenticated && (
              <NavItem>
                <NavLink tag={RouterNavLink} to="/admin/user-management" className="d-flex align-items-center">
                  <FontAwesomeIcon icon={faUsers} />
                  <span>
                    <Translate contentKey="global.menu.admin.userManagement">User Management</Translate>
                  </span>
                </NavLink>
              </NavItem>
            )}
            <LocaleMenu currentLocale={props.currentLocale} onClick={handleLocaleChange} />
            <AccountMenu isAuthenticated={props.isAuthenticated} />
          </Nav>
        </Collapse>
      </Navbar>
    </div>
  );
};

export default Header;
