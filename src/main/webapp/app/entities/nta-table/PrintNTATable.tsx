import React, { useEffect } from 'react';
import { useAppSelector } from 'app/config/store';
import { Table, Button } from 'reactstrap';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';

// import logo from 'path/to/logo.png'; // Replace with your actual logo path

const PrintNTATable = () => {
  const nTATableList = useAppSelector(state => state.nTATable.entities);

  const printTable = () => {
    const input = document.getElementById('printableTable');
    const doc = new jsPDF('landscape');

    html2canvas(input).then(canvas => {
      const imgData = canvas.toDataURL('image/png');
      const imgWidth = 280;
      const imgHeight = (canvas.height * imgWidth) / canvas.width;

      doc.addImage(imgData, 'PNG', 10, 10, imgWidth, imgHeight);
      doc.save('table.pdf');
    });
  };

  useEffect(() => {
    printTable();
  }, []);

  return (
    <div>
      <div className="header">
        <img src="content/images/mcit-logo.png" alt="Logo" className="logo-left" />
        <div className="header-text">
          <p>وزارت مخابرات و تکنالوجی معلوماتی</p>
          <p>ریاست عمومی حکومتداری دیجیتالی</p>
          <p>آمریت تنظیم برنامه ها</p>
        </div>
        <img src="content/images/mcit-logo.png" className="logo-right" />
      </div>
      <hr />
      <div className="table-responsive" id="printableTable">
        <Table responsive>
          <thead>
            <tr>
              <th>Number</th>
              <th>Full Name</th>
              <th>Father Name</th>
              <th>Job Title</th>
              <th>Step</th>
              <th>Education Degree</th>
              <th>Start Date</th>
              <th>End Date</th>
              <th>Salary</th>
              <th>Signature</th>
            </tr>
          </thead>
          <tbody>
            {nTATableList.map((nTATable, i) => (
              <tr key={`entity-${i}`}>
                <td>{nTATable.id}</td>
                <td>{nTATable.fullName}</td>
                <td>{nTATable.fatherName}</td>
                <td>{nTATable.jobTitle}</td>
                <td>{nTATable.step}</td>
                <td>{nTATable.educationDegree}</td>
                <td>{nTATable.startDate}</td>
                <td>{nTATable.endDate}</td>
                <td>{nTATable.salary}</td>
                <td>{nTATable.signature}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>
      <footer className="footer">
        <hr />
        <div className="footer-text">
          <p>وزارت مخابرات و تکنالوجی معلوماتی</p>
          <p>ریاست عمومی حکومتداری دیجیتالی</p>
          <p>آمریت تنظیم برنامه ها</p>
        </div>
      </footer>
    </div>
  );
};

export default PrintNTATable;
