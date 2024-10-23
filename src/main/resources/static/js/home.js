// Datepicker//
$('#rangestart').calendar({
    type: 'date',
    endCalendar: $('#rangeend')
  });
  $('#rangeend').calendar({
    type: 'date',
    startCalendar: $('#rangestart')
  });
  // Datepicker//

  $('#modalstart').calendar({
    type: 'date',
    endCalendar: $('#modalend')
  });
  $('#modalend').calendar({
    type: 'date',
    startCalendar: $('#modalstart')
  });


  $('.ui.calendar')
      .calendar({
          popupOptions: {
               observeChanges: false
          },
        type: 'date',
      }
  );

  // Drop down
   $('.selection.dropdown')
    .dropdown()
  ;
  // Drop down



  $('.fluid.card .image').dimmer({
    on: 'hover'
  });

  /*MODAL*/
  $('.ui.modal').modal({
      allowMultiple: true
    });




  /*TAB*/
  $('.menu .item')
    .tab()
  ;


  /*OWL*/
  $(document).ready(function(){
    $(".owl-carousel").owlCarousel({
      nav:true,
      loop:true,
      items:1,
      margin:5,
      mouseDrag:true,
      responsiveClass:true,
      responsive:{
          0:{
              items:1,
              nav:true
          },
          600:{
              items:2,
              nav:true
          },
          1000:{
              items:1,
              nav:true,
              loop:true
          }
      }
    })
    $( ".owl-prev").html('<i class="fa fa-chevron-left"></i>');
    $( ".owl-next").html('<i class="fa fa-chevron-right"></i>');
  });

  $(document).ready(function(){
    $(".car-2").owlCarousel({
      loop:true,
      items:2,
      margin:5,
      mouseDrag:true,
      responsiveClass:true
    })
  });

  $('.ui.accordion')
    .accordion()
  ;


  // Dropdown Menu
  document.querySelectorAll('.dropdown-toggle').forEach(item => {
    item.addEventListener('click', event => {

      if(event.target.classList.contains('dropdown-toggle') ){
        event.target.classList.toggle('toggle-change');
      }
      else if(event.target.parentElement.classList.contains('dropdown-toggle')){
        event.target.parentElement.classList.toggle('toggle-change');
      }
    })
    });