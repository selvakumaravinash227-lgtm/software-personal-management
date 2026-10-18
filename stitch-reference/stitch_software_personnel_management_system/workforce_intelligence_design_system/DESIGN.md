---
name: Workforce Intelligence Design System
colors:
  surface: '#f8f9ff'
  surface-dim: '#cbdbf5'
  surface-bright: '#f8f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#eff4ff'
  surface-container: '#e5eeff'
  surface-container-high: '#dce9ff'
  surface-container-highest: '#d3e4fe'
  on-surface: '#0b1c30'
  on-surface-variant: '#3e4850'
  inverse-surface: '#213145'
  inverse-on-surface: '#eaf1ff'
  outline: '#6e7881'
  outline-variant: '#bec8d2'
  surface-tint: '#006591'
  primary: '#006591'
  on-primary: '#ffffff'
  primary-container: '#0ea5e9'
  on-primary-container: '#003751'
  inverse-primary: '#89ceff'
  secondary: '#006a61'
  on-secondary: '#ffffff'
  secondary-container: '#86f2e4'
  on-secondary-container: '#006f66'
  tertiary: '#0053db'
  on-tertiary: '#ffffff'
  tertiary-container: '#7397ff'
  on-tertiary-container: '#002c7e'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#c9e6ff'
  primary-fixed-dim: '#89ceff'
  on-primary-fixed: '#001e2f'
  on-primary-fixed-variant: '#004c6e'
  secondary-fixed: '#89f5e7'
  secondary-fixed-dim: '#6bd8cb'
  on-secondary-fixed: '#00201d'
  on-secondary-fixed-variant: '#005049'
  tertiary-fixed: '#dbe1ff'
  tertiary-fixed-dim: '#b4c5ff'
  on-tertiary-fixed: '#00174b'
  on-tertiary-fixed-variant: '#003ea8'
  background: '#f8f9ff'
  on-background: '#0b1c30'
  surface-variant: '#d3e4fe'
  nav-bg: '#0F172A'
  nav-surface: '#1E293B'
  workspace-bg: '#F8FAFC'
  card-bg: '#FFFFFF'
  border-subtle: '#E2E8F0'
  status-success: '#10B981'
  status-warning: '#F59E0B'
  status-danger: '#EF4444'
typography:
  display-lg:
    fontFamily: Inter
    fontSize: 36px
    fontWeight: '700'
    lineHeight: 44px
    letterSpacing: -0.02em
  display-lg-mobile:
    fontFamily: Inter
    fontSize: 28px
    fontWeight: '700'
    lineHeight: 36px
    letterSpacing: -0.01em
  headline-lg:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
    letterSpacing: -0.015em
  headline-md:
    fontFamily: Inter
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
    letterSpacing: -0.01em
  headline-sm:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 24px
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  body-sm:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
  label-lg:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: 0.005em
  label-md:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.01em
  label-sm:
    fontFamily: Inter
    fontSize: 11px
    fontWeight: '600'
    lineHeight: 14px
    letterSpacing: 0.04em
  data-metric:
    fontFamily: Inter
    fontSize: 30px
    fontWeight: '700'
    lineHeight: 36px
    letterSpacing: -0.02em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1.5rem
  gutter-mobile: 1rem
  margin: 2rem
  margin-mobile: 1rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style

This design system targets human capital leaders, enterprise HR business partners, and engineering managers who require effortless oversight of distributed software teams. The interface conveys executive precision, trust, and technological agility. It reconciles high-density organizational telemetry (allocation ratios, performance cycles, attrition risks) with visual clarity and frictionless operation.

The visual direction follows **Corporate / Modern** principles infused with high-performance SaaS functionalism:
- Clean, structured hierarchies prioritizing high information density without cognitive clutter.
- Slate-layered backgrounds that separate administrative control zones (dark persistent navigation) from analytical operational canvases (light, card-based workspaces).
- Micro-interactions that emphasize stability and responsiveness: crisp focus rings, soft layered elevations, and explicit data-density states.
- High-contrast visual tokens for operational statuses (present, pending, off-track, completed) to facilitate glanceable decision-making.

## Colors

The palette leverages high-contrast structural segregation between system chrome and operational telemetry:

- **Primary Action (`#0EA5E9`) & Tertiary (`#2563EB`)**: Anchor interactive controls, focused states, and key navigational links. When paired, they drive high-visibility primary pathways across dashboards and workflow builders.
- **Secondary Accent (`#0D9488`)**: Dedicated to strategic telemetry, organizational growth indicators, and automated intelligence highlights.
- **Navigation Chrome (`#0F172A` & `#1E293B`)**: Forms the global sidebar, context switchers, and elevated dark dropdowns, anchoring the application and minimizing visual vibration against bright monitors.
- **Workspace Canvas (`#F8FAFC`) & Card Surfaces (`#FFFFFF`)**: Establish a low-strain working plane bordered with crisp `#E2E8F0` outlines to isolate data views.
- **Operational Semantic Palette**:
  - `status-success` (`#10B981`): Active, present, approved, retention baseline met.
  - `status-warning` (`#F59E0B`): Pending reviews, capacity thresholds reached, probation checkpoints.
  - `status-danger` (`#EF4444`): Absent, off-track delivery, churn risk, compliance violations.

## Typography

The typography uses **Inter** across all roles to maximize tabular legibility, rendering precision on low-DPI displays, and clean alignment across multi-column data grids.

- **Tabular Figures (`tnum`)**: All numeric readouts, headcount tallies, salary values, and KPI metrics must render with tabular figures enabled to prevent jitter across re-renders and table sorting.
- **Tracking & Weight Hierarchy**:
  - Headings feature slight negative letter-spacing (`-0.01em` to `-0.02em`) to compress visual rhythm at scale.
  - Micro-labels (`label-sm`) use uppercase styling with positive tracking (`0.04em`) to maintain legibility in metadata tags, table headers, and status badges.
- **Hierarchy Enforcements**: Never pair two adjacent headlines of identical weight; use `body-md` in slate-500 (`#64748B`) to anchor auxiliary metadata beneath section headings.

## Layout & Spacing

The layout model is anchored by a persistent collapsible structural shell (260px expanded, 72px condensed) and an adaptive fluid content canvas.

- **Grid Architecture**: 
  - Desktop (≥1280px): 12-column responsive grid with `1.5rem` (24px) gutters and `2rem` (32px) canvas margins.
  - Tablet (768px – 1279px): 8-column layout with `1rem` (16px) gutters, collapsing sidebars into overlay drawers.
  - Mobile (<768px): 4-column layout with `1rem` (16px) outer margins; cards collapse from horizontal multi-attribute layouts into vertical single-column stacks.
- **Spacing Rhythm**: Governed strictly by an 8pt base grid (`0.5rem`, `1rem`, `1.5rem`, `2rem`), with a micro 4pt token (`0.25rem`) reserved for input paddings, chip labels, and compact table cells.
- **Section Padding**: Standard dashboard analytical zones employ `space-xl` (32px) vertical offsets, while contextual toolbars and filter shelves utilize `space-md` (16px) spacing.

## Elevation & Depth

Visual hierarchy uses **tonal layer separation combined with ambient shadows and low-contrast outlines**. Surfaces avoid high-contrast heavy drops in favor of architectural, calm layering:

- **Level 0 (Workspace Canvas)**: `#F8FAFC`, flat.
- **Level 1 (Card & Section Containers)**: `#FFFFFF` background, structural border `1px solid #E2E8F0`, ambient shadow `0 1px 3px 0 rgba(15, 23, 42, 0.04), 0 1px 2px -1px rgba(15, 23, 42, 0.04)`.
- **Level 2 (Dropdowns, Hover States, Popovers)**: `#FFFFFF` surface, border `1px solid #CBD5E1`, elevated shadow `0 4px 6px -1px rgba(15, 23, 42, 0.07), 0 2px 4px -2px rgba(15, 23, 42, 0.05)`.
- **Level 3 (Modals & Command Palettes)**: `#FFFFFF` surface with `0 20px 25px -5px rgba(15, 23, 42, 0.12), 0 8px 10px -6px rgba(15, 23, 42, 0.08)`. Backdrops use `#0F172A` with 50% opacity and 4px Gaussian backdrop-filter blur.
- **Persistent Global Nav**: Sits at `#0F172A` with an inner-edge line `1px solid rgba(255, 255, 255, 0.08)`.

## Shapes

The interface utilizes a **Rounded** (Level 2) shape language, balancing software engineering structure with modern SaaS approachability:

- **Standard Controls (Inputs, Buttons, Dropdowns)**: `0.5rem` (8px) radius.
- **Data Containers (Cards, Table wrappers, Modals)**: `rounded-lg` at `1rem` (16px) outer radius; inner container headers and footers match or taper to `0.75rem` (12px).
- **Badges, Status Pills, and Avatars**: Fully rounded pills (`9999px`) to immediately distinguish categorical state indicators from interactive rectangular controls.
- **Interactive States**: Radius remains stable during interaction (hover, active, focus); feedback is conveyed through border and shadow shifts rather than geometric transformation.

## Components

### Buttons
- **Primary**: Background `#0EA5E9`, text `#FFFFFF`, font `label-lg`. On hover: `#0284C7`. Focused: Ring of `2px` with `#0EA5E9` offset by `2px`.
- **Secondary / Outline**: Background transparent, border `1px solid #CBD5E1`, text `#1E293B`. Hover: `#F1F5F9`.
- **Destructive**: Background `#EF4444`, text `#FFFFFF`. Hover: `#DC2626`.
- **Icon Buttons**: Fixed 36x36px or 40x40px with `rounded-md` (6px–8px) geometry and centered alignment.

### Status Chips & Badges
- **Success (Active/Present)**: Background `rgba(16, 185, 129, 0.12)`, text `#047857`, 1px border `rgba(16, 185, 129, 0.25)`.
- **Warning (Pending/Probation)**: Background `rgba(245, 158, 11, 0.12)`, text `#B45309`, 1px border `rgba(245, 158, 11, 0.25)`.
- **Danger (Absent/Terminated)**: Background `rgba(239, 68, 68, 0.12)`, text `#B91C1C`, 1px border `rgba(239, 68, 68, 0.25)`.
- **Style**: Geometry uses `9999px` pill radius, vertical padding 2px, horizontal padding 8px, font `label-sm`.

### Form Controls (Inputs, Checkboxes, Radios)
- **Text Inputs**: Height 40px, padding `0 12px`, border `1px solid #CBD5E1`, background `#FFFFFF`, font `body-md`. Focus: `#0EA5E9` border with a 3px soft spread of `rgba(14, 165, 233, 0.15)`.
- **Checkboxes & Radios**: 16x16px base, border `1px solid #94A3B8`, active state fills with `#0EA5E9` and a white mark. Focus matches input glow.

### Cards & KPI Stat Blocks
- **Card**: Pure `#FFFFFF` fill, 1px border `#E2E8F0`, padding `1.5rem`.
- **KPI Block**: Includes label (`label-md` in `#64748B`), data value (`data-metric` in `#0F172A`), and an inline pill showing delta (`+4.2% vs last cycle`) using matching semantic success/danger accents.

### Enterprise Tables
- **Header**: Height 44px, background `#F8FAFC`, uppercase `label-sm` in `#475569`, border bottom `1px solid #E2E8F0`.
- **Rows**: Alternating hover fill `#F8FAFC`, default row height 56px, cell font `body-md` in `#1E293B`, bottom cell divider `1px solid #F1F5F9`. Right-aligned action columns feature pinned icon buttons.

### Modals & Dialogs
- Max widths: Small (480px) for confirmation; Large (720px) for employee provisioning.
- Structure: Header with `headline-md`, subtle divider line, scrollable body with `space-lg` padding, and sticky footer containing aligned Secondary (Cancel) and Primary (Confirm) actions.