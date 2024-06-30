import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity } from './nta-table.reducer';
import { faPrint } from '@fortawesome/free-solid-svg-icons';
import jsPDF from 'jspdf';
import vazirFontBase64 from 'app/fonts/vazirFontBase64'; // Import the font
import 'jspdf-autotable'; // Ensure this import is present

export const NTATableDetail = () => {
  const dispatch = useAppDispatch();
  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, [dispatch, id]);

  const nTATableEntity = useAppSelector(state => state.nTATable.entity);

  useEffect(() => {
    console.log('nTATableEntity:', nTATableEntity);
  }, [nTATableEntity]);

  const renderDetailRow = (labelKey, value) => (
    <Col md="6">
      <dt>
        <Translate contentKey={labelKey} />
      </dt>
      <dd>{value}</dd>
    </Col>
  );

  const printTable = () => {
    const doc = new jsPDF({
      orientation: 'landscape',
      unit: 'mm',
      format: 'a4',
    });

    // Embed the custom font
    // Assuming you have the base64 string for vazirFontBase64
    doc.addFileToVFS('Vazir.ttf', vazirFontBase64);
    doc.addFont('Vazir.ttf', 'Vazir', 'normal');
    doc.setFont('Vazir');

    // Add small images on either side of the header
    const leftImage = 'content/images/mcit-logo.png'; // Replace with the correct path if necessary
    const rightImage = 'content/images/download.png'; // Replace with the correct path if necessary

    const imageWidth = 30; // Width of the images
    const imageHeight = 30; // Height of the images
    const imageY = 5; // Y position of the images

    doc.addImage(leftImage, 'PNG', 30, imageY, imageWidth, imageHeight);
    doc.addImage(rightImage, 'PNG', doc.internal.pageSize.width - imageWidth - 30, imageY, imageWidth, imageHeight);

    // Add custom Persian header text
    const headerTexts = ['وزارت مخابرات و تکنالوژی معلوماتی', 'ریاست عمومی حکومتداری دیجیتلی', 'آمریت تنظیم برنامه ها'];
    const fontSize = 12; // Adjust font size as needed

    // Calculate position for centering each text line horizontally
    const pageWidth = doc.internal.pageSize.width;

    headerTexts.forEach((text, index) => {
      const textWidth = (doc.getStringUnitWidth(text) * fontSize) / doc.internal.scaleFactor;
      const textX = (pageWidth - textWidth) / 2;
      const textY = 20 + index * 7; // Adjust vertical positions as necessary
      doc.setFontSize(fontSize);
      doc.text(text, textX, textY);
    });

    // Define columns
    const columns = [
      { header: translate('ntaDatabaseApp.nTATable.signature'), dataKey: 'signature' },
      { header: translate('ntaDatabaseApp.nTATable.salary'), dataKey: 'salary' },
      { header: translate('ntaDatabaseApp.nTATable.endDate'), dataKey: 'endDate' },
      { header: translate('ntaDatabaseApp.nTATable.startDate'), dataKey: 'startDate' },
      { header: translate('ntaDatabaseApp.nTATable.educationDegree'), dataKey: 'educationDegree' },
      { header: translate('ntaDatabaseApp.nTATable.step'), dataKey: 'step' },
      { header: translate('ntaDatabaseApp.nTATable.jobTitle'), dataKey: 'jobTitle' },
      { header: translate('ntaDatabaseApp.nTATable.fatherName'), dataKey: 'fatherName' },
      { header: translate('ntaDatabaseApp.nTATable.fullName'), dataKey: 'fullName' },
      { header: 'Number', dataKey: 'number' },
    ];

    // Define rows
    const rows = [
      {
        number: 1,
        fullName: nTATableEntity.fullName,
        fatherName: nTATableEntity.fatherName,
        jobTitle: nTATableEntity.jobTitle,
        step: nTATableEntity.step,
        educationDegree: nTATableEntity.educationDegree,
        startDate: nTATableEntity.startDate ? new Date(nTATableEntity.startDate).toLocaleDateString('fa-IR') : '',
        endDate: nTATableEntity.endDate ? new Date(nTATableEntity.endDate).toLocaleDateString('fa-IR') : '',
        salary: nTATableEntity.salary,
        signature: nTATableEntity.signature,
      },
    ];

    // Add table
    doc.autoTable({
      columns,
      body: rows,
      startY: 40, // Adjust based on the header height
      margin: { top: 40 },
      styles: {
        font: 'Vazir',
        fontSize: 10,
        cellPadding: 3,
        halign: 'right', // Align text to the right for Persian
      },
      headStyles: {
        font: 'Vazir',
        fontSize: 10,
        cellPadding: 3,
        halign: 'right',
        fillColor: [41, 128, 185],
        textColor: [255, 255, 255],
        fontStyle: 'bold',
      },
    });

    // Add static footer
    doc.text('قرار شرح فوق جدول هذا ترتیب و تقدیم است', 180, doc.internal.pageSize.height - 10); // Adjust as necessary

    // Save the PDF
    doc.save('nta-table.pdf');
  };

  return (
    <Row>
      <Col md="8">
        <h2 data-cy="nTATableDetailsHeading">
          <Translate contentKey="ntaDatabaseApp.nTATable.detail.title">NTATable</Translate>
        </h2>
        <Button className="btnv" onClick={printTable}>
          <FontAwesomeIcon icon={faPrint} />
          <Translate contentKey="ntaDatabaseApp.nTATable.home.print">Print</Translate>
        </Button>
        <dl className="jh-entity-details row">
          {/* {renderDetailRow('global.field.id', nTATableEntity.id)} */}
          {renderDetailRow('ntaDatabaseApp.nTATable.fullName', nTATableEntity.fullName)}
          {renderDetailRow('ntaDatabaseApp.nTATable.fatherName', nTATableEntity.fatherName)}
          {renderDetailRow('ntaDatabaseApp.nTATable.jobTitle', nTATableEntity.jobTitle)}
          {renderDetailRow('ntaDatabaseApp.nTATable.step', nTATableEntity.step)}
          {renderDetailRow('ntaDatabaseApp.nTATable.educationDegree', nTATableEntity.educationDegree)}
          {renderDetailRow(
            'ntaDatabaseApp.nTATable.startDate',
            nTATableEntity.startDate ? <TextFormat value={nTATableEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null,
          )}
          {renderDetailRow(
            'ntaDatabaseApp.nTATable.endDate',
            nTATableEntity.endDate ? <TextFormat value={nTATableEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null,
          )}
          {renderDetailRow('ntaDatabaseApp.nTATable.salary', nTATableEntity.salary)}
          {renderDetailRow('ntaDatabaseApp.nTATable.signature', nTATableEntity.signature)}
        </dl>
        <Button tag={Link} to="/nta-table" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/nta-table/${nTATableEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default NTATableDetail;
